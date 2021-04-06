<script>
  export let coAuthors = [
    "Co-authored-by: ghost <10137+ghost@users.noreply.github.com>",
    "Co-authored-by: ghostbear <10137214+ghostbear@users.noreply.github.com>",
  ];
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
  .commitMessage {
    margin: 0 auto;
    width: max-content;
    max-width: fit-content;
    overflow-wrap: anywhere;
    text-align: left;
    padding: 1em;
    background-color: #e2e2e2;
  }
</style>
