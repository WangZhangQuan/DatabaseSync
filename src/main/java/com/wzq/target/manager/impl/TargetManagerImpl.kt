package com.wzq.target.manager.impl

import com.wzq.target.manager.AbstractTarget
import com.wzq.target.manager.TargetManager
import com.wzq.target.manager.TargetParameter
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class TargetManagerImpl() : TargetManager {

    var pool: MutableMap<TargetParameter, List<AbstractTarget>> = HashMap()
    var readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    override fun get(targetParameter: TargetParameter): AbstractTarget? {
        readWriteLock.readLock().lock()
        try {
            val arrayOfTargets = pool[targetParameter]
            if (arrayOfTargets != null && arrayOfTargets is MutableList<*>) {
                if (!arrayOfTargets.isEmpty()) {
                    val target = arrayOfTargets[arrayOfTargets.size]
                    arrayOfTargets.remove(target)
                    return target
                } else {
                    return createListAndTarget(targetParameter)
                }
            } else {
                return createListAndTarget(targetParameter)
            }
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun recovery(target: AbstractTarget) {
        readWriteLock.writeLock().lock()
        try {
            val list = pool[target.targetParameter]
            if (list is MutableList<AbstractTarget>) {
                list.add(list.size, target)
            }
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }

    private fun createListAndTarget(targetParameter: TargetParameter): AbstractTarget? {
        val arrayList:MutableList<AbstractTarget> = ArrayList()
        arrayList.add(targetParameter.createTarget())
        pool[targetParameter] = arrayList
        return targetParameter.createTarget()
    }
}