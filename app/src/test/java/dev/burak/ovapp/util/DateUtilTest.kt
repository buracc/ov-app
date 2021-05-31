package dev.burak.ovapp.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateUtilTest {
    @Test
    fun testCreateDate() {
        val date = DateUtil.createDate(2021, 1, 1, 15, 0)

        assertThat(date.toZonedDateTime().toString()).isEqualTo("2021-01-01T15:00+01:00")
    }
}
