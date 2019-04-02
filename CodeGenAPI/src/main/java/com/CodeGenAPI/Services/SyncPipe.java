package com.CodeGenAPI.Services;

import java.io.InputStream;
import java.io.OutputStream;

public class SyncPipe implements Runnable {

	private final InputStream istrm_;
	private final OutputStream ostrm_;
	
	public SyncPipe(InputStream istrm,OutputStream ostrm)
	{
		istrm_=istrm;
		ostrm_=ostrm;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			final byte[] buffer=new byte[1024];
			for(int length=0;(length=istrm_.read(buffer))!=-1;)
			{
				ostrm_.write(buffer,0,length);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

}

