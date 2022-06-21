package cctv5.cha.abc123.http

interface OnNetworkRequest {
    fun onSuccess(response: String?)
    fun onFailure(responseCode: Int, responseMessage: String, errorStream: String)
}