package com.example.androidlearnproject

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {
    // this naming convention works here but not in the androidTest package.
    @Test
    fun `empty user name is false`() {
        val result = MyJunitTest.RegistrationUtil.validateRegistrationInputs("", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `user name already exist is false`() {
        val result = MyJunitTest.RegistrationUtil.validateRegistrationInputs("ahmed", "123", "123")
        assertThat(result).isFalse()
    }

    @Test
    fun `password and confirm password not matches is false`() {
        val result =
            MyJunitTest.RegistrationUtil.validateRegistrationInputs("ahmed", "123", "12345")
        assertThat(result).isFalse()
    }

    @Test
    fun `valid user name and correct password and confirmed password is true`() {
        val result = MyJunitTest.RegistrationUtil.validateRegistrationInputs("sayed", "123", "123")
        assertThat(result).isTrue()
    }
}