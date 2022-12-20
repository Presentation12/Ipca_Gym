using LayerBLL.Utils;
using LayerBOL.Models;
using LayerDAL.Services;
using Microsoft.AspNetCore.Mvc;

namespace LayerBLL.Logics
{
    public class AtividadeLogic
    {
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
