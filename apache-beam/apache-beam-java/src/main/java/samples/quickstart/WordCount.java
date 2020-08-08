package samples.quickstart;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.util.Arrays;

public class WordCount {
  public static void main(String[] args) {
    String inputsDir = "data/*";
    String outputsPrefix = "outputs/part";

    PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
    Pipeline pipeline = Pipeline.create(options);

    // Store the word counts in a PCollection.
    // Each element is a KeyValue of (word, count) of types KV<String, Long>.
    PCollection<KV<String, Long>> wordCounts =
        // The input PCollection is an empty pipeline.
        pipeline

        // Read lines from a text file.
        .apply("Read lines", TextIO.read().from(inputsDir))
        // Element type: String - text line

        // Use a regular expression to iterate over all words in the line.
        // FlatMapElements will yield an element for every element in an iterable.
        .apply("Find words", FlatMapElements.into(TypeDescriptors.strings())
            .via((String line) -> Arrays.asList(line.split("[^\\p{L}]+"))))
        // Element type: String - word

        // Keep only non-empty words.
        .apply("Filter empty words", Filter.by((String word) -> !word.isEmpty()))
        // Element type: String - word

        // Count each unique word.
        .apply("Count words", Count.perElement());
        // Element type: KV<String, Long> - key: word, value: counts

    // We can process a PCollection through other pipelines, too.
    // The input PCollection are the wordCounts from the previous step.
    wordCounts
        // Format the results into a string so we can write them to a file.
        .apply("Write results", MapElements.into(TypeDescriptors.strings())
            .via((KV<String, Long> wordCount) ->
                  wordCount.getKey() + ": " + wordCount.getValue()))
        // Element type: str - text line

        // Finally, write the results to a file.
        .apply(TextIO.write().to(outputsPrefix));

    // We have to explicitly run the pipeline, otherwise it's only a definition.
    pipeline.run();
  }
}