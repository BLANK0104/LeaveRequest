package com.example.leavekotlin

import com.example.leavekotlin.loginandcreateuser.LeaveRequest
import com.example.leavekotlin.loginandcreateuser.CreateUserRequest
import com.example.leavekotlin.loginandcreateuser.CreateUserResponse
import com.example.leavekotlin.loginandcreateuser.CurrentRoleandUserResponse
import com.example.leavekotlin.loginandcreateuser.CurrentUserResponse
import com.example.leavekotlin.loginandcreateuser.FacultyAccept
import com.example.leavekotlin.loginandcreateuser.FacultyReject
import com.example.leavekotlin.loginandcreateuser.GatekeeperAccept
import com.example.leavekotlin.loginandcreateuser.LoginRequest
import com.example.leavekotlin.models.LeaveRequestsResponse
import com.example.leavekotlin.loginandcreateuser.HodReject
import com.example.leavekotlin.loginandcreateuser.HodAccept
import com.example.leavekotlin.loginandcreateuser.WardenAccept
import com.example.leavekotlin.models.HistoryResponse
import com.example.leavekotlin.models.LeaveRequestsResponseFaculty
import com.example.leavekotlin.models.LeaveRequestsResponseGatekeeper
import com.example.leavekotlin.models.LeaveRequestsResponseHod
import com.example.leavekotlin.models.LeaveRequestsResponseWarden
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login/login")
    fun login(@Body loginRequest: LoginRequest): Call<CurrentRoleandUserResponse>

    @POST("login/create")
    fun createUser(@Body createUserRequest: CreateUserRequest): Call<CreateUserResponse>

    @GET("login/current")
    fun getCurrentUser(): Call<CurrentUserResponse>

    @POST("leaves/request")
    fun submitLeaveRequest(@Body leaveRequest: LeaveRequest): Call<LeaveRequest>

    @GET("leaves/student/{studentId}")
    fun getLeaveRequests(@Path("studentId") studentId: Int): Call<LeaveRequestsResponse>

    @POST("leaves/faculty/approve/{id}")
    fun approveLeaveByFaculty(@Path("id") id: Int): Call<FacultyAccept>

    @POST("leaves/faculty/reject/{id}")
    fun rejectLeaveByFaculty(@Path("id") id: Int): Call<FacultyReject>
//
   @POST("leaves/hod/approve/{id}")
    fun approveLeaveByHOD(@Path("id") id: Int): Call<HodAccept>

        @POST("leaves/hod/reject/{id}")
   fun rejectLeaveByHOD(@Path("id") id: Int): Call<HodReject>
//
    @POST("leaves/warden/approve/{id}")
   fun approveLeaveByWarden(@Path("id") id: Int): Call<WardenAccept>
//
    @POST("leaves/gatekeeper/approve/{id}")
   fun approveLeaveByGatekeeper(@Path("id") id: Int): Call<GatekeeperAccept>
//
    @GET("leaves/allleave")
    fun getAllLeaveRequests(): Call<AllLeavesFetched>


    //fetching leaves of specific role
    @POST("leave/fetchfaculty")
    fun fetchLeaveRequestToFaculty(@Body leaveRequest: com.example.leavekotlin.models.LeaveRequest): Call<LeaveRequestsResponseFaculty>

    @POST("leave/fetchhod")
    fun fetchLeaveRequestToHOD(@Body leaveRequest: LeaveRequest): Call<LeaveRequestsResponseHod>

    @POST("leave/fetchwarden")
    fun fetchLeaveRequestToWarden(@Body leaveRequest: LeaveRequest): Call<LeaveRequestsResponseWarden>

    @POST("leave/fetchgatekeeper")
    fun fetchLeaveRequestToGatekeeper(@Body leaveRequest: LeaveRequest): Call<LeaveRequestsResponseGatekeeper>


    //history

    //student history
    @GET("history/student/{id}")
    fun getStudentHistory(@Path("id") studentId: Int): Call<HistoryResponse>

}