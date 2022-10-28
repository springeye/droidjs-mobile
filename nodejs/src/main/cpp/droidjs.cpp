// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("droidjs");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("droidjs")
//      }
//    }

#include <jni.h>
#include <string>
#include <cstdlib>
#include "node.h"
#include "redirecting_ouput.h"
#include "uv.h"


int RunNodeInstance(node::MultiIsolatePlatform *pPlatform, std::vector<std::string>& vector1,
                    std::vector<std::string>& vector2);

extern "C"
JNIEXPORT jint  JNICALL
Java_com_github_springeye_droidjs_nodejs_NodeScriptRuntime_startNodeWithArguments(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jobjectArray arguments) {

    //argc
    jsize argument_count = env->GetArrayLength(arguments);

    //Compute byte size need for all arguments in contiguous memory.
    int c_arguments_size = 0;
    for (int i = 0; i < argument_count ; i++) {
        c_arguments_size += strlen(env->GetStringUTFChars((jstring)env->GetObjectArrayElement(arguments, i), 0));
        c_arguments_size++; // for '\0'
    }

    //Stores arguments in contiguous memory.
    char* args_buffer = (char*) calloc(c_arguments_size, sizeof(char));

    //argv to pass into node.
    char* argv[argument_count];

    //To iterate through the expected start position of each argument in args_buffer.
    char* current_args_position = args_buffer;

    //Populate the args_buffer and argv.
    for (int i = 0; i < argument_count ; i++)
    {
        const char* current_argument = env->GetStringUTFChars((jstring)env->GetObjectArrayElement(arguments, i), 0);

        //Copy current argument to its expected position in args_buffer
        strncpy(current_args_position, current_argument, strlen(current_argument));

        //Save current argument start position in argv
        argv[i] = current_args_position;

        //Increment to the next argument's expected position.
        current_args_position += strlen(current_args_position) + 1;
    }
    if (start_redirecting_stdout_stderr()==-1) {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "Couldn't start redirecting stdout and stderr to logcat.");
    }

    //Start node, with argc and argv.
    int node_result = node::Start(argument_count, argv);
    free(args_buffer);


    return jint(node_result);



//    char**  argv2 = uv_setup_args(argument_count, argv);
//    std::vector<std::string> args(argv2, argv2 + argument_count);
//    std::vector<std::string> exec_args;
//    std::vector<std::string> errors;
//    // Parse Node.js CLI options, and print any errors that have occurred while
//    // trying to parse them.
//    int exit_code = node::InitializeNodeWithArgs(&args, &exec_args, &errors);
//    for (const std::string& error : errors)
//        fprintf(stderr, "%s: %s\n", args[0].c_str(), error.c_str());
//    if (exit_code != 0) {
//        return exit_code;
//    }
//
//    // Create a v8::Platform instance. `MultiIsolatePlatform::Create()` is a way
//    // to create a v8::Platform instance that Node.js can use when creating
//    // Worker threads. When no `MultiIsolatePlatform` instance is present,
//    // Worker threads are disabled.
//    std::unique_ptr<node::MultiIsolatePlatform> platform =
//            node::MultiIsolatePlatform::Create(4);
//    v8::V8::InitializePlatform(platform.get());
//    v8::V8::Initialize();
//
//    // See below for the contents of this function.
//    int ret = RunNodeInstance(platform.get(), args, exec_args);
//
//    v8::V8::Dispose();
//    v8::V8::ShutdownPlatform();
//    return ret;


}
using namespace v8;
using namespace node;
int RunNodeInstance(node::MultiIsolatePlatform *platform, std::vector<std::string>& args,
                    std::vector<std::string>& exec_args) {
    int exit_code = 0;

    // Setup up a libuv event loop, v8::Isolate, and Node.js Environment.
    std::vector<std::string> errors;
    std::unique_ptr<node::CommonEnvironmentSetup> setup =
            node::CommonEnvironmentSetup::Create(platform, &errors, args, exec_args);
    if (!setup) {
        for (const std::string& err : errors)
            fprintf(stderr, "%s: %s\n", args[0].c_str(), err.c_str());
        return 1;
    }

    v8::Isolate* isolate = setup->isolate();
    Environment* env = setup->env();

    {
        Locker locker(isolate);
        Isolate::Scope isolate_scope(isolate);
        // The v8::Context needs to be entered when node::CreateEnvironment() and
        // node::LoadEnvironment() are being called.
        Context::Scope context_scope(setup->context());

        // Set up the Node.js instance for execution, and run code inside of it.
        // There is also a variant that takes a callback and provides it with
        // the `require` and `process` objects, so that it can manually compile
        // and run scripts as needed.
        // The `require` function inside this script does *not* access the file
        // system, and can only load built-in Node.js modules.
        // `module.createRequire()` is being used to create one that is able to
        // load files from the disk, and uses the standard CommonJS file loader
        // instead of the internal-only `require` function.
        MaybeLocal<Value> loadenv_ret = node::LoadEnvironment(
                env,
                "const publicRequire ="
                "  require('module').createRequire(process.cwd() + '/');"
                "globalThis.require = publicRequire;"
                "require('vm').runInThisContext(process.argv[1]);");

        if (loadenv_ret.IsEmpty())  // There has been a JS exception.
            return 1;

        exit_code = node::SpinEventLoop(env).FromMaybe(1);

        // node::Stop() can be used to explicitly stop the event loop and keep
        // further JavaScript from running. It can be called from any thread,
        // and will act like worker.terminate() if called from another thread.
        node::Stop(env);
    }

    return exit_code;
}
