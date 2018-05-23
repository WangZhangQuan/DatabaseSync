package com.wzq.target.manager.impl

import com.wzq.target.manager.AbstractTargetX
import com.wzq.target.manager.TargetManager
import com.wzq.target.manager.TargetParameter
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class TargetManagerImpl() : TargetManager {

    val pool: MutableMap<TargetParameter, List<AbstractTargetX>> = HashMap()
    val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    override fun close() {
        readWriteLock.writeLock().lock()
        try {
            for (mutableEntry in pool) {
                for (abstractTargetX in mutableEntry.value) {
                    abstractTargetX.connection.close()
                }
            }
            pool.clear()
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }

    override fun get(targetParameter: TargetParameter): AbstractTargetX? {
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

    override fun recovery(target: AbstractTargetX) {
        readWriteLock.writeLock().lock()
        try {
            val list = pool[target.targetParameter]
            if (list is MutableList<AbstractTargetX>) {
                list.add(list.size, target)
            }
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }

    private fun createListAndTarget(targetParameter: TargetParameter): AbstractTargetX? {
        val arrayList:MutableList<AbstractTargetX> = ArrayList()
        arrayList.add(targetParameter.createTarget(this))
        return targetParameter.createTarget(this)
    }
}