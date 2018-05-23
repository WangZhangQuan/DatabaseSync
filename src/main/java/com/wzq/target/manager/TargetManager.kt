package com.wzq.target.manager

interface TargetManager {
    fun get(targetParameter: TargetParameter): AbstractTargetX?
    fun recovery(target: AbstractTargetX)
    fun close()
}