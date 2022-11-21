package com.ubitar.manager.pqm.group

class Groups private constructor(
    private val mKey: String
) {

    override fun equals(other: Any?): Boolean {
        return other is Groups
                && mKey == other.mKey
    }

    override fun hashCode(): Int {
        return mKey.hashCode()
    }

    companion object {
        fun defaultGroup(): Groups {
            return createGroup("DEFAULT_QUEUE_GROUP")
        }

        fun createGroup(key: String): Groups {
            return Groups(key)
        }

    }

}