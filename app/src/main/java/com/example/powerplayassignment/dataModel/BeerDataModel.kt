package com.example.powerplayassignment.dataModel

/**
 * Data class representing a beer.
 *
 * @property id The unique identifier of the beer.
 * @property name The name of the beer.
 * @property tagline The tagline of the beer.
 * @property description A description of the beer.
 * @property image_url The URL of the image associated with the beer.
 * @property ph The pH level of the beer.
 * @property abv The alcohol by volume (ABV) of the beer.
 */
data class BeerDataModel(
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val image_url:String,
    val ph: Float,
    val abv: Float
)

