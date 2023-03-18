package rap.ung.simastung.API;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rap.ung.simastung.Model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIRequestData {

    @FormUrlEncoded
    @POST("retrieve.php")
    Call<ResponseModel> ardRetrieveData(
            @Field("tipe") String tipe
    );

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("nama") String nama,
            @Field("tipe") String tipe,
            @Field("deskripsi") String deskripsi

    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get.php")
    Call<ResponseModel> ardGetData(
            @Field("id") int id
    );


    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("tipe") String tipe,
            @Field("deskripsi") String deskripsi

    );

    @Multipart
    @POST("upload.php")
    Call<ResponseModel> ardUpload(
            @Part("idAset") RequestBody id,
            @Part MultipartBody.Part imageUpload

    );



    @FormUrlEncoded
    @POST("retrieveKondisi.php")
    Call<ResponseModel> ardRetrieveKondisi(
            @Field("id") int id

    );


    @FormUrlEncoded
    @POST("auth.php")
    Call<ResponseModel> ardLogin(
            @Field("namaPengguna") String namaPengguna,
            @Field("kataSandi") String kataSandi
    );
}
