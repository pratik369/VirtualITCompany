
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.ImportKeyPairRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class CompanyInstance 
{
	String createdInstanceId;
	String mc_no="";
	List<String> instanceIds = new LinkedList<String>();
	String zone="";
	Volume vol = new Volume();
	String volume_info;
	ElasticIPAddress elastic_ip_address=new ElasticIPAddress();
	String ip_address="";
	S3Console s3_console=new S3Console();
	Snapshot snap= new Snapshot();
	
	static Hashtable<String,String> instanceIPs = new Hashtable<String,String>();
	static Hashtable<String,String> snap_ami = new Hashtable<String,String>();
	static Hashtable<String,String> key_pair = new Hashtable<String,String>();
	
	CompanyInstance(String no)
	{
		mc_no=no;
	}
	
	void createInstance(String sgroupname)throws IOException
	{
		
		System.out.println("Current Scenario :");
       	DescribeInstancesResult describeInstancesRequest = Virtualize.ec2.describeInstances();
       	List<Reservation> reservations = describeInstancesRequest.getReservations();
       	Set<Instance> instances = new HashSet<Instance>();
       	// add all instances to a Set.
       	for (Reservation reservation : reservations) 
       	{
       		instances.addAll(reservation.getInstances());
       	}  
       	String imageId1;
       	System.out.println("There are " + instances.size() + " Amazon EC2 instance(s).");
       	for (Instance ins : instances)
       	{           	
       		// instance id
       		String instanceId = ins.getInstanceId();           	
       		// instance state
       		InstanceState is = ins.getState();
       		System.out.println(instanceId+" "+is.getName());
       		// 	System.out.println("Key for the instance  "+keyName);
       	}
       	
        System.out.println("Launching an instance");
        String imageId = "ami-e9ca7f80"; // Basic 32-bit Amazon Linux AMI
        int minInstanceCount = 1; // Creating first instance
        int maxInstanceCount = 1;
        if(!snap_ami.isEmpty())
        {
        	System.out.println("Creating new Instance from the snaphot");
        	imageId1=snap_ami.get(mc_no);
        }
        RunInstancesRequest rir = new RunInstancesRequest(imageId, minInstanceCount, maxInstanceCount);
        rir.withInstanceType("t1.micro");
        rir.getSecurityGroups().add(sgroupname);
        RunInstancesResult result = Virtualize.ec2.runInstances(rir);
        
        //get instanceId from the result
        List<Instance> resultInstance = result.getReservation().getInstances();
        
        for (Instance ins : resultInstance)
        {
        	createdInstanceId = ins.getInstanceId(); 
        	//System.err.println(createdInstanceId);
        	instanceIds.add(createdInstanceId);
        	zone = ins.getPlacement().getAvailabilityZone();
        	System.out.println("Employee "+mc_no+" gets a machine: "+ins.getInstanceId() +" in zone " + zone);
        //    System.out.println("getKeyName() is: "+ ins.setKeyName(myEC2key));
        }
        System.out.println("Creating a 'tags' for the new instances.");
        List<String> resources = new LinkedList<String>();
        List<Tag> tags = new LinkedList<Tag>();
        String s="EmployeeMachine"+mc_no;
        //System.err.println(s);
        Tag nameTag = new Tag("Name",s );
        
        resources.add(createdInstanceId);
        tags.add(nameTag);
        
        CreateTagsRequest ctr = new CreateTagsRequest(resources, tags);
        Virtualize.ec2.createTags(ctr);
	}
	
    		
//	void startInstance()
//	{
//		System.out.println("#7 Stop the Instance");
//        List<String> instanceIds = new LinkedList<String>();
//        instanceIds.add(createdInstanceId);
//        
//        //start
//        StartInstancesRequest startIR = new StartInstancesRequest(instanceIds);
//        ec2.startInstances(startIR);
//	}
//	
//	void stopInstance()
//	{
//		List<String> instanceIds = new LinkedList<String>();
//        instanceIds.add(createdInstanceId);
//        
//        //stop
//        StopInstancesRequest stopIR = new StopInstancesRequest(instanceIds);
//        ec2.stopInstances(stopIR);
//	}
	
	void terminateInstance()
	{
	    System.out.println(instanceIds);
        TerminateInstancesRequest tir = new TerminateInstancesRequest(instanceIds);
        Virtualize.ec2.terminateInstances(tir);
        //ec2.shutdown();
        createdInstanceId="";
    	mc_no="";
    	instanceIds = new LinkedList<String>();
    	zone="";
    	vol = new Volume();
    	volume_info="";
    	elastic_ip_address=new ElasticIPAddress();
    	ip_address="";
    	s3_console=new S3Console();
    	snap= new Snapshot();
	}
	
	void createVolumeForInstance()
	{
		volume_info = vol.create(createdInstanceId,Virtualize.ec2,zone);
	}
	 
	void attachVolumeForInstance()
	{
		vol.attachVolume(createdInstanceId, Virtualize.ec2, volume_info);
	}
	
	void detachVolumeForInstance()
	{
		vol.detachVolume(createdInstanceId, Virtualize.ec2, volume_info);
	}
	
	void allocateIpAddressForInstance()
	{
		ip_address=elastic_ip_address.allocateIpAddress(createdInstanceId,Virtualize.ec2);
		instanceIPs.put(mc_no,ip_address);
	}
	 
	void associateIpAddressForInstance()
	{
		if(ip_address.isEmpty() && instanceIPs.containsValue(mc_no))
		{
			instanceIPs.get(mc_no);
		}
		elastic_ip_address.associateIpAddress(createdInstanceId,Virtualize.ec2,ip_address);
	}
	
	void dissociateVolumeForInstance()
	{
		elastic_ip_address.dissociateIpAddress(createdInstanceId,Virtualize.ec2,ip_address);
	}
	
	void createBucketForInstance()throws IOException
	{
		s3_console.createS3Bucket(Virtualize.s3,mc_no);
	}
	
	void createSecurityKey(AmazonEC2 ec2)throws IOException
	{
		//Create Key Pair
		CreateKeyPairRequest skreq = new CreateKeyPairRequest();
		String KeyName="EmployeeSecurityKey"+mc_no+Virtualize.no_of_days;
		skreq.setKeyName(KeyName);
		key_pair.put(mc_no, KeyName);
	    CreateKeyPairResult kp = ec2.createKeyPair(skreq);
	    KeyPair keyPair= kp.getKeyPair();   
	    BufferedWriter out = new BufferedWriter(new FileWriter(KeyName+".pem"));
	    out.write(keyPair.getKeyMaterial());
	    out.close();
	    //System.out.println("Exception in writing key file");     
	} 
	
	void createSnapshot ()throws Exception
	{
		snap_ami.put(mc_no,snap.create(Virtualize.ec2, createdInstanceId, mc_no));
	}
}

   