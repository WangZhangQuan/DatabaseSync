package com.wzq.target.manager

abstract class TargetParameter {
    abstract fun createTarget(): AbstractTarget
    abstract override fun equals(other: Any?): Boolean
}