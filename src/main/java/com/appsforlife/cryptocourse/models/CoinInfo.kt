package com.appsforlife.cryptocourse.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coin_info_list")
data class CoinInfo(

    @PrimaryKey
    @SerializedName("Id")
    val id: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("ImageUrl")
    val imageUrl: String,
    @SerializedName("Url")
    val url: String,
    @SerializedName("Algorithm")
    val algorithm: String,
    @SerializedName("ProofType")
    val proofType: String,
    @SerializedName("DocumentType")
    val documentType: String,
    @SerializedName("AssetLaunchDate")
    val assetLaunchDate: String

) {
    fun getFullImageURL(): String {
        return "https://cryptocompare.com$imageUrl"
    }

    fun getFullCoinUrl(): String {
        return "https://cryptocompare.com$url"
    }
}