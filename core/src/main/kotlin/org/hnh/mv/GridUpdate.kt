package org.hnh.mv

sealed interface GridUpdate {
    data object LineComplete : GridUpdate
    data object LineIncomplete : GridUpdate
    data object NotAllowed : GridUpdate
    data object GridFull : GridUpdate
}
