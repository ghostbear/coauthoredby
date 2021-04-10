<script>
  export let coAuthors = [];
  $: commitMessage = coAuthors.join("<br>");

  function copyToClipboard() {
    navigator.clipboard.writeText(coAuthors.join("\n"));
    alert("Snippet copied to clipboard")
  }
</script>

{#if commitMessage}
  <div class="commitMessage">
    <h2>Message snippet</h2>
    <i>Click to copy to clipboard</i>
    <div class="codeBlock">
      <code
      id="CommitMessage"
      on:click={copyToClipboard}
      contenteditable="false"
      bind:innerHTML={commitMessage}
    />
    </div>
    
  </div>
{/if}

<style>
  .commitMessage {
    margin-top: 3em;
  }

  .codeBlock {
    background-color: #e2e2e2;
    padding: 1em
  }
</style>
