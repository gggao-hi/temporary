data class FeatureFlagStatusCollection(
    val romVersion: Long,
    val appVersion: Long,
    val mapStreamingAndADMStatus: FeatureFlagStatus<MapStreamingAndADMStatus>,
    val vrStatus: FeatureFlagStatus<Boolean>
)

data class FeatureFlagStatus<T : Any>(val enableRuntimeApply: Boolean, val status: T)

sealed class MapStreamingAndADMStatus {
    object Close : MapStreamingAndADMStatus()
    object Open : MapStreamingAndADMStatus()
    object OnlyStreaming : MapStreamingAndADMStatus()
}