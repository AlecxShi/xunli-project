package com.xunli.manager.web;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MethodHealth implements HealthIndicator
{
	public static long time2 = System.currentTimeMillis();

	@Override
	public Health health()
	{
		//System.out.println("time2:--------------->"+time2);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println("tim2----1----->"+df.format(time2));
		long time1 = System.currentTimeMillis();
		//System.out.println("time1:--------------->"+time1);
		//System.out.println("time1----1----->"+df.format(time1));
		long time3 = time1 - time2;
		//System.out.println("time3--------->"+time3);
		if (time3 > 60000)
		{
			return new Health.Builder().withDetail("userLogin", "timeout").status("500").down().build();
		}
		else
		{
			return new Health.Builder().withDetail("userLogin", "timeout").up().build();
		}

	}

}
