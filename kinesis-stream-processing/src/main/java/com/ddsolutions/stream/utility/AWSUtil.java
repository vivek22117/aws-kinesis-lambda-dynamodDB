package com.ddsolutions.stream.utility;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class AWSUtil {
    private static final Logger LOGGER = LogManager.getLogger(AWSUtil.class);

    private static AWSCredentialsProvider awsCredentials;

    public static AmazonSQS getSQSClient() {
        try {
            awsCredentials = getAWSCredentials();
            return AmazonSQSClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching sqs aws credentials " + ex.getMessage());
            throw ex;
        }
    }

    public static AmazonSNS getSNSClient(){
        try{
            awsCredentials = getAWSCredentials();
            return AmazonSNSClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        }catch (Exception ex){
            LOGGER.error("Exception occurred while creating SNS client");
            throw ex;
        }
    }

    public static AmazonDynamoDB getDynamoDBClient() {
        try {
            awsCredentials = getAWSCredentials();
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching dynamodb aws credentials " + ex.getMessage());
            throw ex;
        }
    }

    public static AmazonCloudWatchClient getCloudwatchClient() {
        try {
            AWSCredentialsProvider awsCredentials = getAWSCredentials();
            return (AmazonCloudWatchClient) AmazonCloudWatchClientBuilder.standard()
                    .withCredentials(awsCredentials)
                    .withRegion(Regions.US_EAST_1).build();
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while fetching cloudwatch aws credentials " + ex.getMessage());
            throw ex;
        }
    }

    private static AWSCredentialsProvider getAWSCredentials() {
        if (awsCredentials == null) {
            boolean isRunningInLambda = Boolean.parseBoolean(PropertyLoader.getPropValues("isRunningInLambda"));
            if (isRunningInLambda) {
                awsCredentials = new EnvironmentVariableCredentialsProvider();
            } else {
                awsCredentials = new ProfileCredentialsProvider("doubledigit");
            }
        }
        return awsCredentials;
    }

}
