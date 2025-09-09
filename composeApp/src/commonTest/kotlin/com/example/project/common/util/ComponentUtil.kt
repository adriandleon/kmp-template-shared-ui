package com.example.project.common.util

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.kotest.matchers.types.shouldBeTypeOf

internal fun <T : Any> createComponentForTest(factory: (ComponentContext) -> T): T {
    val lifecycle = LifecycleRegistry()
    val component = factory(DefaultComponentContext(lifecycle = lifecycle))
    lifecycle.resume()

    return component
}

internal inline fun <reified T : Any> Value<ChildStack<*, *>>.assertActiveInstance() {
    value.assertActiveInstance<T>()
}

internal inline fun <reified T : Any> ChildStack<*, *>.assertActiveInstance() {
    active.instance.shouldBeTypeOf<T>()
}

internal inline fun <reified T : Any> Value<ChildStack<*, *>>.activeInstance(): T =
    active.instance as T

// Slot navigation utilities
internal inline fun <reified T : Any> Value<ChildSlot<*, *>>.assertActiveSlotInstance() {
    value.assertActiveSlotInstance<T>()
}

internal inline fun <reified T : Any> ChildSlot<*, *>.assertActiveSlotInstance() {
    child?.instance?.shouldBeTypeOf<T>()
}

internal inline fun <reified T : Any> Value<ChildSlot<*, *>>.activeSlotInstance(): T =
    value.child?.instance as T
