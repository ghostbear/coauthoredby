<script>
  import { Octokit } from "@octokit/core";
  import CommitMessage from "./CommitMessage.svelte";

  let coAuthors = [{ username: "ghost" }];

  let coAuthorSnippets = [];

  function add() {
    coAuthors = coAuthors.concat([{ username: "" }]);
  }

  function reset() {
    coAuthors = [{ username: "" }];
    coAuthorSnippets = [];
  }

  function remove(selectedCoAuthor) {
    coAuthors = coAuthors.filter((coAuthor) => coAuthor !== selectedCoAuthor);
  }

  function submit() {
    Promise.all(client.getAuthors(coAuthors)).then((values) => {
      coAuthorSnippets = values.map(({ data: { name, login, id } }) => {
        return `Co-authored-by: ${name ?? login} <${id}+${login}@users.noreply.github.com>`;
      });
    });
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
  <h1>Co-authored-by</h1>
  <p>
    Simple tool to generate snippets for commit messages to easily give credit to users who help you with a commit on GitHub
  </p>
  <br />
  <b>What is the username of the co-author(s)?</b>
  <div class="coAuthors">
    {#each coAuthors as coAuthor}
      <div class="coAuthor">
        <input placeholder="username" bind:value={coAuthor.username} />
        <button on:click={remove(coAuthor)}> ❌ </button>
      </div>
    {/each}
  </div>

  <div>
    <button on:click={add}> Add </button>
    <button on:click={reset}> Reset </button>
    <button on:click={submit}> Submit </button>
  </div>
  <p>Click <b>submit</b> to generate snippet</p>

  <CommitMessage coAuthors={coAuthorSnippets} />

  <p class="footer">
    Made by <a href="https://github.com/ghostbear">ghostbear</a>
  </p>
</main>

<style>
  main {
    max-width: 720px;
    margin: 0 auto;
  }

  .footer {
    margin-top: 1.75em;
  }
</style>
