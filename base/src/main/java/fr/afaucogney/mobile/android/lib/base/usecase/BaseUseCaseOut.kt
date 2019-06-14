package fr.afaucogney.mobile.android.lib.base.usecase

interface BaseUseCaseOut<out OUT> {
    fun execute(): OUT
}
