import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object

fun V8Object.register(javaObj:Any) {
    javaObj::class.java.methods.forEach {method->
        registerJavaMethod(javaObj,method.name,method.name,method.parameterTypes)
    }
}
fun V8.getV8Object(javaObj:Any): V8Object {
    val obj= V8Object(this)
    obj.register(javaObj)
    return obj
}