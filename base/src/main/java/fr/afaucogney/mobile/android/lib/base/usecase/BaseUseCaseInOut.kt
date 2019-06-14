package fr.afaucogney.mobile.android.lib.base.usecase

interface BaseUseCaseInOut<in IN, out OUT> {
    fun execute(parameter: IN): OUT
}
