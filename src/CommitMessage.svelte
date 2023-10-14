<script>
  export let coAuthors = [];
  $: commitMessage = coAuthors.join("<br>");

  function copyToClipboard() {
    navigator.clipboard.writeText(coAuthors.join("\n")).then(() => {
        alert("Snippet copied to clipboard")
      })
  }
</script>

{#if commitMessage}
  <div class="commitMessage">
    <h2>Message snippet</h2>
    <i>Click to copy to clipboard</i>
    <div on:click={copyToClipboard} class="codeBlock">
      <code
      id="CommitMessage"
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
