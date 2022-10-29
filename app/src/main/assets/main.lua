require 'app'
print("hello lua")
--app.launch("com.tencent.wx")
local pkgName=app.getPackageName("微信")
print("getPackageName==>" , pkgName)
print("getAppName====>" , app.getAppName(pkgName))


