package com.github.springeye.droidjs.modules

interface IUi {
    fun findByText(text:String): IUiNode?

}
interface IUiNode {
    fun click()

}