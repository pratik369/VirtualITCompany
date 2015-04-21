import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;


public class Snapshot 
{	
	public String create(AmazonEC2 ec2,String instanceID, String mc_no ) throws Exception
	{
       	CreateImageRequest cir = new CreateImageRequest();
 		cir.setInstanceId(instanceID);
 		cir.setName("New_AMI"+mc_no+Virtualize.no_of_days);	
 		CreateImageResult createImageResult = ec2.createImage(cir);
 		String createdImageId = createImageResult.getImageId();
 		return createdImageId; 			
	}
}

