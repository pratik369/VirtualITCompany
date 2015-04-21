import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.s3.AmazonS3Client;

public class Volume
{
	AmazonS3Client s3;
	public String create(String instanceId, AmazonEC2 ec2, String zone)
	{
		//create a volume
		CreateVolumeRequest cvr = new CreateVolumeRequest();
	    cvr.setAvailabilityZone(zone);
	    cvr.setSize(4); 
	 	CreateVolumeResult volumeResult = ec2.createVolume(cvr);
	 	return (volumeResult.getVolume().getVolumeId());
	}
	
	public void attachVolume(String instanceId,AmazonEC2 ec2, String vol_info)
	{
		AttachVolumeRequest avr = new AttachVolumeRequest();
	 	avr.setVolumeId(vol_info);
	 	avr.setInstanceId(instanceId);
	 	avr.setDevice("/dev/sdf");
	 	ec2.attachVolume(avr);
	}
	public void detachVolume(String instanceId, AmazonEC2 ec2, String vol_info )
	{
		DetachVolumeRequest dvr = new DetachVolumeRequest();
		dvr.setVolumeId(vol_info);
		dvr.setInstanceId(instanceId);
		ec2.detachVolume(dvr);
	}
}
