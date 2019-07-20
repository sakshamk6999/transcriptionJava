import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class pushkar implements Runnable {
    //    @Override
    public void run() {

        FileInputStream credentials = null;
        try {
            credentials = new FileInputStream("D:/downloads/steveKey.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GoogleCredentials cred = null;
        try {
            cred = GoogleCredentials.fromStream(credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(cred);
        SpeechSettings speechSettings = null;
        try {
            speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {
            // The path to the audio file to transcribe
            String fileName = "D:/downloads/audioSample3.wav";

            // Reads the audio file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
