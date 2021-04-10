<script>
  export let coAuthors = [];
  $: commitMessage = coAuthors.join("<br>");

  function copyToClipboard() {
    navigator.clipboard.writeText(coAuthors.join("\n"));
  }
</script>

{#if commitMessage}
  <div class="commitMessage">
    <code
      id="CommitMessage"
      on:click={copyToClipboard}
      contenteditable="false"
      bind:innerHTML={commitMessage}
    />
  </div>
{/if}

<style>
</style>
