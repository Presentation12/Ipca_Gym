using LayerBLL.Utils;
using LayerBOL.Models;
using LayerDAL.Services;
using Microsoft.AspNetCore.Mvc;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Atividade
    /// </summary>
    public class AtividadeLogic
    {
        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as atividades
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Atividade> atividadeList = await AtividadeService.GetAllService(sqlDataSource);

            if (atividadeList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de atividades obtida com sucesso";
                response.Data = new JsonResult(atividadeList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma atividade em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Atividade que é pretendida retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Atividade atividade = await AtividadeService.GetByIDService(sqlDataSource, targetID);

            if (atividade != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Atividade obtida com sucesso";
                response.Data = new JsonResult(atividade);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma atividade
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newAtividade">Objeto com os dados da Atividade a ser criada</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Atividade newAtividade)
        {
            Response response = new Response();
            bool creationResult = await AtividadeService.PostService(sqlDataSource, newAtividade);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Atividade obtida com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma atividade
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="atividade">Objeto que contém os dados atualizados da Atividade</param>
        /// <param name="targetID">ID da Atividade que é pretendida atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Atividade atividade, int targetID)
        {
            Response response = new Response();
            bool updateResult = await AtividadeService.PatchService(sqlDataSource, atividade, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Atividade alterada com sucesso");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma atividade
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID da Atividade que é pretendida eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await AtividadeService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Atividade removida com sucesso");
            }

            return response;
        }
    }
}
