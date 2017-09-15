package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.ILoginDataManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit2.Retrofit;

/**
 * LoginDataManagerTest
 */
public class LoginDataManagerTest {

    @Mock
    Retrofit mRetrofit;

    private ILoginDataManager mLoginDataManager;

    @Before
    public void setUp() throws Exception {
        this.mLoginDataManager = new LoginDataManager(mRetrofit);
    }

    @Test
    public void testRegister() throws Exception {
        

    }
}