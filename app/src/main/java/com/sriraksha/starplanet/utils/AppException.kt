package com.sriraksha.starplanet.utils

/**
 * Exception thrown when there is no network connection available.
 * Optionally, a custom error message can be provided.
 */
class NoNetworkException(message: String = "No network connection available") : Exception(message)

/**
 * Exception thrown when an error occurs in the remote data source.
 * The error message should be provided when creating an instance of this exception.
 */
class RemoteDataSourceException(message: String) : Exception(message)

/**
 * Exception thrown when there are no more pages available to load.
 * Optionally, a custom error message can be provided.
 */
class NoMorePagesException(message: String = "No more pages available") : Exception(message)