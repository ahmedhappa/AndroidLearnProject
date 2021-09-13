package com.example.androidlearnproject.dependency_injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
//ActivityComponent will make this class contained by the Activity class context.
@InstallIn(ActivityComponent::class)
/* ## Hilt Modules cannot contain both non-static and abstract binding methods,
 so you cannot place @Binds and @Provides annotations in the same class. */
abstract class AbstractModule {

    /* @Binds must annotate an abstract function (since it's abstract, it doesn't contain any code and the class needs
    to be abstract too)
     */
    //we could use the same logic with @Provide annotation in object class but this is a different way
    //this function will produce different object every time because it is in the activity container and it can't be singleton
    @Binds
    abstract fun provideMyInjectableInterface(myInjectableInterfaceImpl: MyInjectableInterfaceImpl): MyInjectableInterface
}