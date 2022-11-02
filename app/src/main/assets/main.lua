require 'app'
require 'ui'
require 'device'
print("hello lua")
toast('toast test')
app.launch("com.tencent.wx")
local pkgName=app.getPackageName("微信")
if pkgName ~=nil then

    print("getPackageName==>" , pkgName)
    print("getAppName====>" , app.getAppName(pkgName))
else
    print("没有找到微信")
end
backHome()
sleep(1000)
local node=ui.findByText("信息")
if node ~= nil then
    node:click()
    sleep(1000)
    local node2=ui.findByText("开始聊天")
    if node2 ~= nil then
        node2:click()
        sleep(1000)
        --sendKey(35)
        --sendKey(36)
        sendText('你好')
    end
end



