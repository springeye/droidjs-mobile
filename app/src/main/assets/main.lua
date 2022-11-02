require 'app'
require 'ui'
print("hello lua")
--app.launch("com.tencent.wx")
local pkgName=app.getPackageName("微信")
if pkgName ~=nil then

    print("getPackageName==>" , pkgName)
    print("getAppName====>" , app.getAppName(pkgName))
else
    print("没有找到微信")
end
backHome()
system:sleep(1500)
local node=ui.findByText("Gmail")
if node==nil then
    print("没有找到节点")
else
    print(node)
    node:click()
end



