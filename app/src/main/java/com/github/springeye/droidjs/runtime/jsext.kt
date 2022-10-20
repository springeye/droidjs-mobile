import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.js.JsModule

//fun V8Object.register(javaObj:Any) {
//    javaObj::class.java.methods.forEach {method->
//        registerJavaMethod(javaObj,method.name,method.name,method.parameterTypes)
//    }
//}
//fun V8.getV8Object(javaObj:Any): V8Object {
//    val obj= V8Object(this)
//    obj.register(javaObj)
//    return obj
//}
fun V8.registerJavaObject(name:String,module: JsModule): V8Object {
    val v8Obj= V8Object(this)
    add(name,v8Obj)
    module::class.java.methods.forEach {method->
        v8Obj.registerJavaMethod(module,method.name,method.name,method.parameterTypes)
    }
    return v8Obj

}
fun Any.toV8Object(v8:V8):V8Object{
    val obj=V8Object(v8)
    this::class.java.methods.forEach {method->
        obj.registerJavaMethod(this,method.name,method.name,method.parameterTypes)
    }
    return obj
}