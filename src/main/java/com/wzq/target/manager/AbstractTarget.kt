package com.wzq.target.manager

import com.wzq.core.connector.Target

abstract class AbstractTarget : Target {
    abstract var targetParameter: TargetParameter
}