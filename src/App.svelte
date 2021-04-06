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
      coAuthorSnippets = values.map(({ data: { login, id } }) => {
        return `Co-authored-by: ${login} <${id}+${login}@users.noreply.github.com>`;
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
        <button on:click={remove(coAuthor)}> ❌ </button>
      </div>
    {/each}
  </div>

  <div>
    <button on:click={add}> Add new </button>
    <button on:click={reset}> Reset </button>
    <button on:click={submit}> Submit </button>
  </div>

  <CommitMessage coAuthors={coAuthorSnippets} />
</main>

<style>
  main {
    text-align: center;
    padding: 1em;
    max-width: 640px;
    margin: 0 auto;
  }

  .co-author {
    display: flex;
    justify-content: center;
  }

  h1 {
    color: #ff3e00;
    text-transform: uppercase;
    font-size: 4em;
    font-weight: 100;
  }

  @media (max-width: 640px) {
    main {
      max-width: none;
    }

    h1 {
      font-size: 4em;
    }
  }

  @media (max-width: 480px) {
    h1 {
      font-size: 3em;
    }
  }

  @media (max-width: 380px) {
    h1 {
      font-size: 2.75em;
    }
  }

  @media (max-width: 320px) {
    h1 {
      font-size: 2.5em;
    }
  }

  @media (max-width: 280px) {
    h1 {
      font-size: 2em;
    }
  }
</style>
