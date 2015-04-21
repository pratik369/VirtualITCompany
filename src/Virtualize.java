import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;


public class Virtualize
{
	public static int no_of_days=1;
	static AmazonEC2 ec2;
	static AmazonS3Client s3;
	public static void main(String[] args) throws Exception
	{
		AWSCredentials credentials = new PropertiesCredentials(CompanyInstance.class.getResourceAsStream("AwsCredentials.properties")); 
		ec2 = new AmazonEC2Client(credentials); 
		s3= new AmazonS3Client(credentials);
		CompanyInstance instance_employee_1,instance_employee_2;
	    System.out.println("========================================================================");
	    System.out.println("      Welcome to the Virtual IT Company- Powered by Amazon AWS !!");
	    System.out.println("========================================================================\n");
	    System.out.println("Since you are the IT administrator for a large Enterprise, you reach a minute early !! ");
		
	    //CREATE SECURITY GRP
    	System.out.println("Enabling Security Policies: Creating Security group for the instances");
    	Security sec_employee=new Security();
    	String sgroupname=sec_employee.createSG(ec2); // Putting the instances in the same security group	    

	    while(no_of_days <= 2)
	    {
	    	instance_employee_1=new CompanyInstance("1");
	    	instance_employee_2=new CompanyInstance("2");
	    	
	    	System.out.println("Day "+no_of_days+" will start in a minute, Please show patience..");
			Thread.sleep(6000);
		    System.out.println("**********************************************************************");
	
	    	System.out.println("Day "+no_of_days+" Starts:\n Now, Making the resources available for first employee");
	    		    
	    	instance_employee_1.createInstance(sgroupname); // Creating Instance - EmployeMachine1
	    	instance_employee_1.createSecurityKey(ec2); //Creating Individual Keys for each Instance
	    	
	    	System.out.println("Now, Making the resources available for second employee");
	    	instance_employee_2.createInstance(sgroupname); // Creating Instance - Employee Machine 2
	    	instance_employee_2.createSecurityKey(ec2); //Creating Individual Keys for each Instance
	    	Thread.sleep(50000);
	    	System.out.println("-----> Performing Actions for Instance 1 ");
	    	System.out.println("Creating Volume....");
	    	instance_employee_1.createVolumeForInstance();
	    	System.out.println("Attaching Volume....");
	    	instance_employee_1.attachVolumeForInstance();
	    	
	    	if(no_of_days == 1)
	    	{
	    		System.out.println("Allocating the Ip Address....");
	    		//CREATE AN IP ADDRESS ONLY FOR THE FIRST DAY AND REUSE THE SAME IP FOR OTHER DAYS
	    	}
	    	System.out.println("Associating the Ip Address....");
	    	instance_employee_1.allocateIpAddressForInstance();
	    	instance_employee_1.associateIpAddressForInstance();
	    	
	    	System.out.println("Creating S3 Bucket....");
	    	instance_employee_1.createBucketForInstance();
	    	
	    	System.out.println("----->> Performing Actions for Instance 2 ");
	    	System.out.println("Creating Volume....");
	    	instance_employee_2.createVolumeForInstance();
	    	System.out.println("Attaching Volume....");
	    	instance_employee_2.attachVolumeForInstance();
	    	
	    	if(no_of_days == 1)
	    	{
	    		System.out.println("Allocating the Ip Address....");
	    	}	
	    	instance_employee_2.allocateIpAddressForInstance();
	    	System.out.println("Associating the Ip Address....");
	    	instance_employee_2.associateIpAddressForInstance();
	    	
	    	System.out.println("Creating S3 Bucket....");
	    	instance_employee_2.createBucketForInstance();
	    	
	    	System.out.println("DONE !!");
	    	System.out.println("*****************************************************************");
	    	System.out.println("Employees work very hard throughout the day");
	    	Thread.sleep(360000); 
	    	System.out.println("*****************************************************************");
	    	System.out.println("Day "+no_of_days+" Ends : Bye !! Go Home");
	    	System.out.println("Employees Leave their cabin, so take away their machines");
	    	
	    	System.out.println("Creating Snapshot for instances");
	    	instance_employee_1.createSnapshot();
	    	instance_employee_2.createSnapshot();
	    	
	    	System.out.println("Detaching Volumes for all instances");
	    	instance_employee_1.detachVolumeForInstance();
	    	instance_employee_2.detachVolumeForInstance();
	    	
	    	System.out.println("Dissociating the Ip Address");
	    	instance_employee_1.dissociateVolumeForInstance();
	    	instance_employee_2.dissociateVolumeForInstance();
	    	System.out.println("Terminating the Instances");
	    	instance_employee_1.terminateInstance();
	    	instance_employee_2.terminateInstance();
	    	System.out.println("Night Time !!  ");
	    	System.out.println("Good Night, Sleep Well !!  ");
	    	Thread.sleep(120000);
	    	no_of_days++;
	    }
	}
}
