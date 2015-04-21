
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
 * Modified by Sambit Sahu
 * Modified by Kyung-Hwa Kim (kk2515@columbia.edu)
 * 
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class S3Console {

    AmazonS3Client s3;
         
         // We assume that we've already created an instance. Use the id of the instance.
         String instanceId = "i-5a9fa127"; //put your own instance id to test this code.
         void createS3Bucket(AmazonS3Client s3 , String mc_no)throws IOException
         {
        	 this.s3  = s3;
             
             //create bucket
             String bucketName = "prachi-amazon-s3-bucket-"+mc_no+Virtualize.no_of_days;
             s3.createBucket(bucketName);
             
             //set key
             String key = "ReadMe.txt";
             
             //set value
             File file = File.createTempFile("temp", ".txt");
             file.deleteOnExit();
             Writer writer = new OutputStreamWriter(new FileOutputStream(file));
             writer.write("Amazon S3 Bucket Created.\r ........Success !!");
             writer.close();
             
             //put object - bucket, key, value(file)
             s3.putObject(new PutObjectRequest(bucketName, key, file));
             
             //get object
             S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
             BufferedReader reader = new BufferedReader(
             	    new InputStreamReader(object.getObjectContent()));
             String data = null;
             while ((data = reader.readLine()) != null) {
                 System.out.println(data);
             }  
    }
}