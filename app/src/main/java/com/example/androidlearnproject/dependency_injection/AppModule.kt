package com.example.androidlearnproject.dependency_injection

import android.content.Context
import com.example.androidlearnproject.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton

//qualifier is used to decide which object instance to provide if there is 2 functions provide the same type of object
//qualifier is used during the initialization and during the injection time
@Qualifier
annotation class CurrentDateCalendar

@Qualifier
annotation class CustomDateCalendar

/* Modules are used to add bindings to Hilt, or in other words, to tell Hilt how to provide instances of different types.
In Hilt modules, you can include bindings for types that cannot be constructor-injected such as interfaces or classes that
are not contained in your project
 */
//tells Hilt that this is a module and
@Module
/*tells Hilt the containers where the bindings are available by specifying a Hilt component.
 You can think of a Hilt component as a container*/
//SingletonComponent will make this class contained by the application class context.
//full list of components https://developer.android.com/training/dependency-injection/hilt-android#generated-components
@InstallIn(SingletonComponent::class)
object AppModule {

    /* We can annotate a function with @Provides in Hilt modules to tell Hilt how to provide types the return of the
    function is what hilt will return */
    //this function will run every time and provide a different object of date because it is not singleton
    @Provides
    fun provideDate(@CurrentDateCalendar calendar: Calendar): Date {
        return calendar.time
    }

    @CurrentDateCalendar
    //this function will run only one time and produce the same object every time because it is annotated with singleton
    @Singleton
    @Provides
    fun provideCalendar(): Calendar {
        return Calendar.getInstance()
    }

    @CustomDateCalendar
    @Provides
    @Singleton
/* Each Hilt container comes with a set of default predefined bindings that can be injected as dependencies into
your custom bindings. This is the case with applicationContext. To access it, you need to annotate
the field with @ApplicationContext */
// list of predefined bindings in the https://developer.android.com/training/dependency-injection/hilt-android#component-default
    fun provideCalendarAnother(@ApplicationContext applicationContext: Context): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(2022, 8, applicationContext.resources.getInteger(R.integer.day_of_month))
        return calendar
    }
}