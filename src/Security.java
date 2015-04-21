/*
 * Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * 
 * Modified by Kyung-Hwa Kim (kk2515@columbia.edu)
 * Modified by Sambit Sahu
 * 
 */

import java.io.IOException;
import java.util.ArrayList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.SecurityGroup;


public class Security 
{	
	String securitygroup="";
	String createSG(AmazonEC2 ec2)throws IOException
	{
		try 
		{		
			securitygroup="VirualIT_Security_Group"+Virtualize.no_of_days;
			CreateSecurityGroupRequest reqsec =  new CreateSecurityGroupRequest().withGroupName(securitygroup).withDescription("ssh-tcp-https-http");
			CreateSecurityGroupResult ressec =ec2.createSecurityGroup(reqsec);
			String ipAddr = "0.0.0.0/0";
			ArrayList<String> ipRanges = new ArrayList<String>();
			ipRanges.add(ipAddr);
			ArrayList<IpPermission> ipPermissions = new ArrayList<IpPermission> ();
			IpPermission ipPermission_ssh = new IpPermission();
			ipPermission_ssh.setIpProtocol("tcp");
			ipPermission_ssh.setFromPort(new Integer(22));
			ipPermission_ssh.setToPort(new Integer(22));
			IpPermission ipPermission_http = new IpPermission();
			ipPermission_http.setIpProtocol("tcp");
			ipPermission_http.setFromPort(new Integer(80));
			ipPermission_http.setToPort(new Integer(80));
			IpPermission ipPermission_https = new IpPermission();
			ipPermission_https.setIpProtocol("tcp");
			ipPermission_https.setFromPort(new Integer(443));
			ipPermission_https.setToPort(new Integer(443));		
			ipPermission_ssh.setIpRanges(ipRanges);
			ipPermission_http.setIpRanges(ipRanges);
			ipPermission_https.setIpRanges(ipRanges);
			ipPermissions.add(ipPermission_http);
			ipPermissions.add(ipPermission_https);
			ipPermissions.add(ipPermission_ssh);
			try 
			{
				// Authorize the ports to the used.
				AuthorizeSecurityGroupIngressRequest ingressRequest = new AuthorizeSecurityGroupIngressRequest(securitygroup,ipPermissions);
				ec2.authorizeSecurityGroupIngress(ingressRequest);
				System.out.println("Assigned "+ingressRequest);
			} 
			catch (AmazonServiceException ase) 
			{
				// Ignore because this likely means the zone has already been authorized.
				System.err.println(ase.getMessage());
			}
			DescribeSecurityGroupsRequest x = new DescribeSecurityGroupsRequest().withGroupNames(securitygroup);
			DescribeSecurityGroupsResult secgrp = ec2.describeSecurityGroups(x);
			for (SecurityGroup s:secgrp.getSecurityGroups())
			{
				if(s.getGroupName().equals(securitygroup))
				{
					System.out.println(s.getIpPermissions());
				}
			}
		} 
		catch (AmazonServiceException ase) 
		{
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}
	
		return securitygroup;
		}
}
