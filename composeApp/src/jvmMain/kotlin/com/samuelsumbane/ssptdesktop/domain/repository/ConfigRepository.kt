package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.SysConfigItem

interface ConfigRepository {
    suspend fun getConfigs(): List<SysConfigItem>
    suspend fun getPackageName(): StatusAndMessage
    suspend fun editConfig(sysConfigItem: SysConfigItem): StatusAndMessage
}