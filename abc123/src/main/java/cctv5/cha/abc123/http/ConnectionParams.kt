package cctv5.cha.abc123.http

import cctv5.cha.abc123.http.Connection


class ConnectionParams {
    private var connection: Connection? = null

    fun setConnectionInstance(connectionInstance: Connection) {
        connection = connectionInstance
    }

    fun ofTypeGet(): Connection {
        connection!!.setRequestType("GET")
        return connection!!
    }

    fun ofTypePost(): Connection {
        connection!!.setRequestType("POST")
        return connection!!
    }

}