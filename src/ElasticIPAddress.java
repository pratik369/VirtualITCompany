import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AllocateAddressResult;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.DisassociateAddressRequest;


public class ElasticIPAddress 
{
	String allocateIpAddress(String instanceId , AmazonEC2 ec2)
	{
		AllocateAddressResult elasticResult = ec2.allocateAddress();
		String elasticIp = elasticResult.getPublicIp();
		System.out.println("New elastic IP: "+elasticIp);
		return elasticIp;
	}
	void associateIpAddress(String instanceId, AmazonEC2 ec2,String ip_address)
	{
		AssociateAddressRequest aar = new AssociateAddressRequest();
		aar.setInstanceId(instanceId);
		aar.setPublicIp(ip_address);
		ec2.associateAddress(aar);
	}
	void dissociateIpAddress(String instanceId , AmazonEC2 ec2, String ip_address )
	{
		DisassociateAddressRequest dar = new DisassociateAddressRequest();
		dar.setPublicIp(ip_address);
		ec2.disassociateAddress(dar);
	}
}
