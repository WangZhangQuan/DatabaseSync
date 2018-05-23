package com.wzq.target.manager

abstract class TargetParameter(user: String, pass: String, url: String, driver: String) {

    var user: String = user
    var pass: String = pass
    var url: String = url
    var driver: String = driver

    abstract fun createTarget(targetManager: TargetManager): AbstractTargetX
    abstract override fun equals(other: Any?): Boolean
}