package com.appsforlife.cryptocourse.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coin_info_list")
data class CoinInfo(

    @SerializedName("Id")
    val id: String,
    @PrimaryKey
    @SerializedName("Name")
    val name: String,
    @SerializedName("FullName")
    val fullName: String? = null,
    @SerializedName("ImageUrl")
    val imageUrl: String? = null,
    @SerializedName("Url")
    val url: String? = null,
    @SerializedName("Algorithm")
    val algorithm: String? = null,
    @SerializedName("ProofType")
    val proofType: String? = null,
    @SerializedName("DocumentType")
    val documentType: String? = null,
    @SerializedName("AssetLaunchDate")
    val assetLaunchDate: String? = null

) {
    fun getFullImageURL(): String {
        return "https://cryptocompare.com$imageUrl"
    }

    fun getFullCoinUrl(): String {
        return "https://cryptocompare.com$url"
    }
}