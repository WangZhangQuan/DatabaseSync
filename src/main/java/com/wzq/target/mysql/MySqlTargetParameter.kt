package com.wzq.target.mysql

import com.wzq.target.manager.AbstractTargetX
import com.wzq.target.manager.TargetManager
import com.wzq.target.manager.TargetParameter
import net.minidev.json.JSONObject
import java.sql.DriverManager

class MySqlTargetParameter(user: String, pass: String, url: String, driver: String) : TargetParameter(user, pass, url, driver) {

    private val params = JSONObject(mapOf<String, String>(Pair("user", user), Pair("url", url), Pair("diver", driver))).toJSONString()

    override fun createTarget(targetManager: TargetManager): AbstractTargetX {
        Class.forName(driver)
        return MySqlTarget(targetManager, DriverManager.getConnection(url, user, pass), this)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MySqlTargetParameter) {
            return params == other.params
        }
        return false
    }
}