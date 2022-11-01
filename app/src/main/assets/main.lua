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
local node=ui.findByText("匹配图片")
if node==nil then
    print("没有找到节点")
else
    print(node)
end

--node.click()


