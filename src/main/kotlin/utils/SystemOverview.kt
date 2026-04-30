/*
 * SPDX-FileCopyrightText: ©2026 HOE Team
 * SPDX-License-Identifier: MIT
 *
 * Project: NNETB
 */

package utils

data class SystemOverview(
    val osVersion: String,
    val architecture: String,
    val windowsUpdateStatus: String,
    val platform: String,
    val computerName: String,
    val wallpaperPath: String?
)
