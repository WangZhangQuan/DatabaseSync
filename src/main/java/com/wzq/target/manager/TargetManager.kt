package com.wzq.target.manager

interface TargetManager {
    fun get(targetParameter: TargetParameter): AbstractTarget?
    fun recovery(target: AbstractTarget)
}