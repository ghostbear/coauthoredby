<script>
  import { Octokit } from "@octokit/core";

  let coAuthors = [{ username: "ghost" }];

  let coAuthorSnippets = [];

  $: commitMessageSnippet = coAuthorSnippets.join("<br>");

  function add() {
    coAuthors = coAuthors.concat([{ username: "" }]);
  }

  function reset() {
    coAuthors = [{ username: "" }];
  }

  function remove(selectedCoAuthor) {
    coAuthors = coAuthors.filter((coAuthor) => coAuthor !== selectedCoAuthor);
  }

  function submit() {
    Promise.all(client.getAuthors(coAuthors)).then((values) => {
      coAuthorSnippets = values.map(({ data: { login, id } }) => {
        return `Co-authored-by: ${login} <${id}+${login}@users.noreply.github.com>`;
      });
    });
  }

  function copyToClipboard() {
    navigator.clipboard.writeText(coAuthorSnippets.join("\n"));
  }

  const client = (() => {
    const octokit = new Octokit();
    const authors = new Map();
    return {
      getAuthors: (list) => {
        return list.map((author) => {
          return (authors[author.username] =
            authors[author.username] ??
            octokit.request("GET /users/{username}", {
              username: author.username,
            }));
        });
      },
    };
  })();
</script>

<main>
  <h1>Co-authored</h1>
  <p>
    Simple tool to give credit to users who help you with a commit on GitHub
  </p>
  <br />
  <b>What is the username of the co-author(s)?</b>
  <div class="co-authors">
    {#each coAuthors as coAuthor}
      <div class="co-author">
        <input placeholder="username" bind:value={coAuthor.username} />
        <button on:click={remove(coAuthor)}> Remove </button>
      </div>
    {/each}
  </div>

  <div>
    <button on:click={add}> Add new </button>
    <button on:click={reset}> Reset </button>
    <button on:click={submit}> Submit </button>
  </div>

  {#if coAuthorSnippets}
    <code
      id="commit-message-snippet"
      on:click={copyToClipboard}
      contenteditable="false"
      bind:innerHTML={commitMessageSnippet}
    />
  {/if}
</main>

<style>
  main {
    text-align: center;
    padding: 1em;
    max-width: 240px;
    margin: 0 auto;
  }

  h1 {
    color: #ff3e00;
    text-transform: uppercase;
    font-size: 4em;
    font-weight: 100;
  }

  @media (min-width: 640px) {
    main {
      max-width: none;
    }
  }
</style>
